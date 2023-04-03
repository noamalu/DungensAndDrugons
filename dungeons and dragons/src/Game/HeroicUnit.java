package Game;

import Enemy.Enemy;

import java.util.List;

public interface HeroicUnit {
    public void castAbility(List<Enemy> enemies);
    void streamAbility();
}
