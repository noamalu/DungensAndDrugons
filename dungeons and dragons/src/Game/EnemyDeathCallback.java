package Game;

import Enemy.Enemy;

public interface EnemyDeathCallback{
	void call(Enemy e);
}