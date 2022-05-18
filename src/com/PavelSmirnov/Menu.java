
//Строка меню в игре

package com.PavelSmirnov;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.Serial;

public class Menu extends JMenuBar{

    public Menu () {

        JMenu game = new JMenu("Игра");
        this.add(game);
        JMenuItem newGame = new JMenuItem("Новая игра");
        JMenuItem rules = new JMenuItem("Правила");
        JMenuItem about = new JMenuItem("Об игре");
        about.addActionListener(e -> Message.about());
        newGame.addActionListener(e -> {
            Checker.side=1;
            ChessBoard.newGame();
            Message.startGame();
        });
        rules.addActionListener(e -> Message.rules());
        game.add(newGame);
        game.add(rules);
        game.add(about);
        game.add(new JSeparator());
        JMenuItem exit = new JMenuItem(new ExitAction());

        game.add(exit);

    }

    //Вложенный класс завершения работы приложения
    static class ExitAction extends AbstractAction
    {
        @Serial
        private static final long serialVersionUID = 1L;
        ExitAction() {
            putValue(NAME, "Выход");
        }
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }


}
