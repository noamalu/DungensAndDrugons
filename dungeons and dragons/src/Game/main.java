package Game;
import java.util.Scanner;


public class main {


    public static void main(String[] args) {
        String path = "C:\\Users\\malul\\OOPAssignment3\\levels_dir";
        GameManager game = new GameManager(path);
        game.Play();
    }
}
