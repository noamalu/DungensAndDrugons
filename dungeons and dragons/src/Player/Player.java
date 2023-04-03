package Player;
import java.util.List;
import Enemy.Enemy;
import Game.*;
import GameTiles.Unit;
import Objects.Position;

public abstract class Player extends Unit implements HeroicUnit{
    protected final int REQ_EXP = 50;
    protected final int ATTACK_BONUS = 4;
    protected final int DEFENCE_BONUS = 1;
    protected final int HEALTH_BONUS = 10;
    protected int experience;
    protected int level;

    public void streamAbility(){
        messageCallback.send(skill);
    }
    public String getSkill() {
        return skill;
    }

    protected String skill;
    protected PlayerDeathCallBack deathCallBack;
    public Player(Position pos, String name, int healthPool, int attackPoints, int defencePoints){
        super(pos,'@',name,healthPool,attackPoints,defencePoints);
        this.experience = 0;
        this.level = 1;
    }

    public Player initialize(Position position, MessageCallback messageCallback, PlayerDeathCallBack playerDeathCallBack){
        super.initialize(position, messageCallback);
        this.deathCallBack =playerDeathCallBack;
        return this;
    }

    public void addExp (int experienceGained){
        this.experience = this.experience+experienceGained;
        int nextLevelReq= levelUpReq();
        while (experience>=nextLevelReq){
            levelUp();
            experience = experience-nextLevelReq;
            nextLevelReq = levelUpReq();
        }
    }
    protected void levelUp(){
        level++;
        int healthGained = gainHealth();
        int attackGained = gainAttack();
        int defenceGained = gainDefence();
        setHealthPool(getHealthPool()+healthGained);
        setHealthAmount(getHealthPool());
        setAttackPoints(getAttackPoints()+attackGained);
        setDefensePoints(getDefensePoints()+defenceGained);
        messageCallback.send(String.format("%s reached level %d: +%d Health, +%d Attack +%d Defence", getName(),getLevel(),healthGained, attackGained, defenceGained));
    }
    public abstract void castAbility(List<Enemy> enemies);
    @Override
    public void accept(Unit unit) {
        unit.visit(this);
    }

    @Override
    public void processStep() {

    }

    @Override
    public void visit(Player p) {
    }

    @Override
    public void visit(Enemy e) {
        this.battle(e);
    }


    public void onKill (Enemy e){
        int experienceGained = e.getExperience();
        addExp(experienceGained);
        messageCallback.send(String.format("%s died. %s gained %d experience", e.getName(), getName(), experienceGained));
    }

    @Override
    public void onDeath(){
        messageCallback.send("You lost.");
        deathCallBack.call();
    }
    protected int gainHealth(){
        return level* HEALTH_BONUS;
    }
    protected int gainAttack(){
        return level*ATTACK_BONUS;
    }
    protected int gainDefence(){
        return level*DEFENCE_BONUS;
    }
    protected int levelUpReq(){
        return level*REQ_EXP;
    }
    public String describe(){
        return super.describe()+String.format("\tLevel: %d\t\tExperience: %d/%d" ,getLevel(), getExperience(),levelUpReq());
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
