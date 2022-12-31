package com.jafadog.games;

import com.jafadog.Terminal;
import com.jafadog.android.console.Cartridge;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company:
 *
 * @author
 * @version 1.0
 */

public class Kroz extends Cartridge {

    public static final int KEY_LEFT = 1037;
    public static final int KEY_UP = 1038;
    public static final int KEY_RIGHT = 1039;
    public static final int KEY_DOWN = 1040;
    public Terminal terminal;

    int[] colMap = { 0, 1, 2, 2, 4, 5, 1, 2, 7, 1, 8, 9, 4, 10, 11, 12 };
    int kackp = -1;
    String lo = "@";
    int gewon = -1;
    int gewon2 = -1;
    int gewon3 = -1;
    int gewon4 = -1;
    int gewon5 = -1;
    int gewon6 = -1;
    int gewon7 = -1;

    int level = 1;
    int ax = 4;

    int ay = 7;
    int punkte = 0;

    int pam = 0;

    public Kroz(){
        terminal = getTerminal();
    }


    private void anfang() {
        setLevel();

        schritt(0, 0);
        // start:
        while (true) {

            String ts = "" + terminal.read();
            if ("".equalsIgnoreCase(ts))
                continue;
            if (ts.toLowerCase().equals("q")) {
                return;
            }

            int x = 0;
            int y = 0;

            // System.out.println("pressed" + (int)ts.charAt(0));

            int curser = (int) ts.charAt(0);

            if (curser == this.KEY_LEFT)
                x = -1;
            if (curser == this.KEY_UP)
                y = -1;
            if (curser == this.KEY_RIGHT)
                x = 1;
            if (curser == this.KEY_DOWN)
                y = 1;

            if (x == 0 && y == 0)
                continue;
            // System.out.println("found "+ getChar(ay + y, ax + x));
            if (getChar(ay + y, ax + x) == ' ') {
                schritt(x, y);
            } else if (getChar(ay + y, ax + x) == 'K') {
                kackp = kackp + 5;
                kakies();
                schritt(x, y);
            } else if (getChar(ay + y, ax + x) == '.') {
                punkt();
                schritt(x, y);
            } else if (getChar(ay + y, ax + x) == '@' && kackp > 0) {
                kackp = kackp - 1;
                kakies();
                schritt(x, y);

            } else if (getChar(ay + y, ax + x) == 'P') {

                lo = " ";
                schritt(x, y);
            }
        }
    }

    public void anleitung() {
        terminal.clearScreen();
        setForegroundColor(4);
        terminal.println("Anleitung:");
        terminal.println("");
        terminal.println("");
        setForegroundColor(12);
        terminal.println("In diesem Spiel bist du KROZ (der Held) der die Welt von den boesen, boesen  ");
        terminal.println("Punkten befreien muß.XX");
        terminal.println("");
        terminal.println("");
        terminal.println("");
        setForegroundColor(4);
        terminal.println("Und wie geht das? Man esse einfach soviel wie moeglich Punkte im jeweiligen");
        terminal.println("Level. Dabei gibt es jedoch einen Nachteil. KROZ (der Held) tut auf seinem Weg");
        terminal.println("immer Kueddeln und versperrt somit den Durchgang (außer mit PAMPERS (P)). Durch");
        terminal.println("diese Kueddel kann KROZ (der Held) nur noch gehen wenn er genuegend KACKIS (K)");
        terminal.println("besitzt.");
        terminal.println("");
        setForegroundColor(15);
        terminal.println("");
        terminal.println("");
        terminal.println("");
        terminal.println("Du steuerst KROZ (den Held) mit den CURSOR Tasten.");
        terminal.println("");
        terminal.println("");
        terminal.println("");
        terminal.println("");
        setForegroundColor(14);
        terminal.println("                   (Beliebige Taste zum Fortfahren !!!)");
        terminal.sleep();
    }

    public void ende() {
        terminal.clearScreen();
        // END
    }

    public void finale() {
        terminal.clearScreen();
        // CLEAR
        // COLOR 2
        terminal.println("");
        terminal.println("                                    GEWONNEN");
        terminal.println("");
        terminal.println("             Du hast den sechsten und somit letzten Level bestanden.");
        terminal.println("");
        terminal.println("");
        // COLOR 15
        terminal.println("       KROZ (der Held) befreit die Welt von den boesen, boesen Punkten !!!");
        // COLOR 14
        terminal.println("");
        terminal.println("");
        terminal.println("                       (Beliebige Taste zum Fortfahren)");
        terminal.sleep();
        // GOTO s:

    }

    private char getChar(int y, int x) {
        return terminal.getChar(x, y);
    }

    public void go() {
        terminal.setKeyboardDelay(50);
        terminal.clearScreen();
        menue();
    }


    private void kakies() {
        // SOUND 50, 1
        setPos(2, 8);

        terminal.println("");
        terminal.println(" " + (kackp) + " ");

    }

    private void level1() {
        terminal.clearScreen();

        lo = "@";
        gewon = 59;
        ax = 4;
        ay = 7;

        setForegroundColor(14);
        terminal.println("    KROZ");
        terminal.println("");
        setForegroundColor(12);
        terminal.println(" 0   Gefressene PUNKTE (von 59)                            (Q)uit            ");
        terminal.println(" 0   Verbleibende KACKIS");
        terminal.println("");
        setForegroundColor(9);
        terminal.println("████████████████████████████████████████████████████████████████████████████████");
        terminal.println("███    ██              .           █████████████████████████████████████████████");
        terminal.println("███    ██  █████████████████  ███     .          .      ████████████████████████");
        terminal.println("████. ██   █████████████████ .████ . █████████████████   ███████████████████████");
        terminal.println("████ .█   ██████████████████  █████ . █████████████████ .    ███████████████████");
        terminal.println("████.   .███████████████████  ██████                 ██████ .             ██████");
        terminal.println("█████████████ .     .     .      .    █████   █████  ███████   █████████  ██████");
        terminal.println("████████████   ███████████  ██████████████   ██████  ████████ .  . █████  ██████");
        terminal.println("████████████  ████████████  █████████████   ███████  ███████ . ██  █████  ██████");
        terminal.println("████████████ .████████ .... █████████      ████████           ███ .       ██████");
        terminal.println("████            ██████ .██. ████████   █████████████████████████   ███  ████████");
        terminal.println("████..█████ ██  ██████ .... ███████  .     .               ████   ████  ████████");
        terminal.println("████   ████ ██   █████████  ██████   ███████  ███████████   ██   █████  ████████");
        terminal.println("█████..      ██   ████████  █████   ████████ .████████████    . ██████  ████████");
        terminal.println("██████   ███ ███            ..     █████████ ..█████████ .. ██████████  ████████");
        terminal.println("███████.  ██ █████████   ████████████████████   ..  ███.. ████████████  ████████");
        terminal.println("██       . .. . .  ..                              ..    █████          ████████");
        terminal.println("████████████████████████████████████████████████████████████████████████████████");

    }

    public void level2() {
        terminal.clearScreen();
        lo = "@";
        gewon2 = 30;
        ax = 2;
        ay = 7;

        setForegroundColor(15);
        terminal.println("    KROZ");
        terminal.println("");
        setForegroundColor(9);
        terminal.println(" 0  Gefressene PUNKTE (von 30)                            (Q)uit            ");
        terminal.println(" 0  Verbleibende KACKIS");
        terminal.println("");
        setForegroundColor(3);
        terminal.println("████████████████████████████████████████████████████████████████████████████████");
        terminal.println("█              █████████       █████████████████████████████████████████████████");
        terminal.println("█              █████████       ███████████████  .               .     ██████████");
        terminal.println("█                              ███████████████ ██████████████████████ ██████████");
        terminal.println("█              ███████████████.███████████████ ██████████████████████.██████████");
        terminal.println("██████████████████████████████ ██████████          ██████████████████.██████████");
        terminal.println("████████████████████████      .██████████     .    ██████████████████ ██████████");
        terminal.println("███████████████████████        ██████████          ██████████████████ ██████████");
        terminal.println("██ .       .               .     .     .                   ██████         ██████");
        terminal.println("██             .█████████████████████████             .    ██████     .   ██████");
        terminal.println("██.     K       █████████████████████████████████████████  ██████         ██████");
        terminal.println("██      K      .███████████P█████████████████████████████ .██████████████    ███");
        terminal.println("██.            .██ .         .                 ██████████████████████████   .███");
        terminal.println("██         .    ███████████ ███████████████  .      █████████████████████    ███");
        terminal.println("███████████████████████████ ███████████████      .           .               ███");
        terminal.println("████████████████████  .   .       .████████         ████████████████████████████");
        terminal.println("████████████████████████████████████████████████████████████████████████████████");
        terminal.println("████████████████████████████████████████████████████████████████████████████████");
    }

    public void level3() {
        terminal.clearScreen();
        lo = "@";
        gewon3 = 1;
        ax = 2;
        ay = 7;

        setForegroundColor(15);
        terminal.println("    KROZ");
        terminal.println("");
        setForegroundColor(9);
        terminal.println(" 0  Gefressene PUNKTE (von 1 )                            (Q)uit            ");
        terminal.println(" 0  Verbleibende KACKIS");
        terminal.println("");
        setForegroundColor(3);
        terminal.println("████████████████████████████████████████████████████████████████████████████████");
        terminal.println("██                        ███ ████           █████ ████ ██                   ███");
        terminal.println("██  █████████ ██████ ████ ███      ███ █████               █████████ ████ ██ ███");
        terminal.println("██ ██ █     █ █████████ █ ████████ ████████████████████ ████            █ ██ ███");
        terminal.println("██ ██ █ ███ █        ██ █                           ███ ████████████ ████ ██ ███");
        terminal.println("██ ██ █ █ █ ████████ ██ █ ████ ██████████ ████ ████              ███ ████    ███");
        terminal.println("██ ██ █   █          ██   ████             ███    █ ████████████ ████████ ██████");
        terminal.println("██    █████████████ ██████████████████ █████ ██████            █     ██     ████");
        terminal.println("█████████████         ████                       █████████████ █████ ██        █");
        terminal.println("███   ███     █████ ███    ████ ████████████████ ██          █     █    ██     █");
        terminal.println("███ █████████ █████ ███    ██████ ██          █████ ████████ █████ ██████████ K█");
        terminal.println("███           █ ███ ██████ █         █ ██ ███          █ ███  █ ██ █        ████");
        terminal.println("█████████████ █ █   █      █ ██████████████████████ ████ ████   ██ █ ██████ ████");
        terminal.println("██            █ ███ ██████ █ ███ █                  ██     ████ ██ ████     ████");
        terminal.println("██ ████████████ ██         █     █ ████████████████ ██ ████████ ██      ████████");
        terminal.println("██ ██           ████████████████ █           █████████     ████ ████████████  .█");
        terminal.println("██    █████████                  ███ ███ ███           ██████           @@@@   █");
        terminal.println("████████████████████████████████████████████████████████████████████████████████");
    }

    public void level4() {
        terminal.clearScreen();
        lo = "@";
        gewon4 = 94;
        ax = 2;
        ay = 7;

        setForegroundColor(14);
        terminal.println("    KROZ");
        terminal.println("");
        setForegroundColor(12);
        terminal.println(" 0  Gefressene PUNKTE (von 94)                            (Q)uit            ");
        terminal.println(" 0  Verbleibende KACKIS");
        terminal.println("");
        setForegroundColor(9);

        terminal.println("████████████████████████████████████████████████████████████████████████████████");
        terminal.println("█         █                                                                  ███");
        terminal.println("█████████ █   ████████████ █ █████████████████████████████████          ████ ███");
        terminal.println("█.......  █   ████████████ █ ████████████████████████████████████  ███..████ ███");
        terminal.println("█ ███████ █   ████████████   ██████████████████████████ . ███████  █.    .██ ███");
        terminal.println("█.......  █   ████████████ . ██████████████████████████   ███████  █.    .██ ███");
        terminal.println("█████████ █   █████████████████████████████████████████ █ ███████  ███..████ ███");
        terminal.println("█████████ █   █████████████████████████████████████████ █ ███████  █.    .██ ███");
        terminal.println("█             █████████████████████████████████████████ █ ███████  █.    .██ ███");
        terminal.println("█                                                                  ███..████ ███");
        terminal.println("█ █ ██ █ ███                                                       █.    .██ ███");
        terminal.println("█ █ ██ █ ███     █     █     █     █     █     █     █     █     ███. .. .██ ███");
        terminal.println("█   ██   ███ .   █  .  █   . █ .   █  .  █   . █ .   █  .  █   . █████  ████ ███");
        terminal.println("█ . ██ . ███.....█.....█.....█.....█.....█.....█.....█.....█.....███      ██ ███");
        terminal.println("████████████████████████████████████████████████████████████████████      ██ ███");
        terminal.println("█.      █                  █ █           █     █              █           ██ ███");
        terminal.println("█.    █    █  █ █       █      █      █     █     █   █ █   █   █            ███");
        terminal.println("████████████████████████████████████████████████████████████████████████████████");
    }

    public void level5() {
        terminal.clearScreen();
        lo = "@";
        gewon5 = 47;
        ax = 3;
        ay = 7;

        setForegroundColor(15);
        terminal.println("    KROZ");
        terminal.println("");
        setForegroundColor(9);
        terminal.println(" 0  Gefressene PUNKTE (von 47)                            (Q)uit            ");
        terminal.println(" 0  Verbleibende KACKIS");
        terminal.println("");
        setForegroundColor(3);

        terminal.println("████████████████████████████████████████████████████████████████████████████████");
        terminal.println("██             █████████████████████████████████████████████████████████████████");
        terminal.println("██                     █████████████████████████████████████████████████████████");
        terminal.println("██             ███████ █████████████████████████████████████████████████████████");
        terminal.println("██████████████████████ █████████████████████████████████████████████████████████");
        terminal.println("█████ . .   ██████ .     . ██████   .   █████ .  .  ████████████████████████████");
        terminal.println("█████.              .   .         .  .         . . .              ██████████████");
        terminal.println("█████  . .  ██████.        ██████  .   .█████ .  .  █████████████ ██████████████");
        terminal.println("████████ █████████████ █████████████ ███████████ ████████████████ ██████████████");
        terminal.println("████████ █████████████ █████████████ ███████████ █████████████    .   ██████████");
        terminal.println("████████ █████████   .     █████████ ███████████ █████████████  .     ██████████");
        terminal.println("████████           .    .                                        .    ██████████");
        terminal.println("████████ █████████  .  .   █████████████████████ █████████████ .   .  ██████████");
        terminal.println("████████ ███████████████████████████████████████ █████████████   .    ██████████");
        terminal.println("████ .   .   █████  .    .  ████████████████████ █████████████     .  ██████████");
        terminal.println("████  .            .    .                        ███████████████████████████████");
        terminal.println("████.    .  .█████ .  .   . ████████████████████████████████████████████████████");
        terminal.println("████████████████████████████████████████████████████████████████████████████████");
    }

    public void level6() {
        terminal.clearScreen();
        lo = "@";
        gewon6 = 43;
        ax = 2;
        ay = 7;

        setForegroundColor(14);
        terminal.println("    KROZ");
        terminal.println("");
        setForegroundColor(12);
        terminal.println(" 0  Gefressene PUNKTE (von 43)                            (Q)uit            ");
        terminal.println(" 0  Verbleibende KACKIS");
        terminal.println("");
        setForegroundColor(9);
        terminal.println("████████████████████████████████████████████████████████████████████████████████");
        terminal.println("█             █████████████████....                       ██████████████████████");
        terminal.println("█              ████████████████....         ████████████  ██████████████████████");
        terminal.println("█             ........  ███████....     ██ ██████████████ ██████████████████████");
        terminal.println("██████████████████████  ███████....███ ███ ██████████████ ██████████████████████");
        terminal.println("███████████████████████ ██████████████ ███.██████████████ ██████████████████████");
        terminal.println("██████████████████████   █████████████ ██████████████████ ██████████████████████");
        terminal.println("███... ████                            █████████████              ██████████████");
        terminal.println("███ ...████ K██████████ █████████████  █████████████  ██████████████████████████");
        terminal.println("████  █████ ███████████ ██████████████ █████....████ ███████████████████████████");
        terminal.println("████  █████ ███████████            ... █████    ████  ██████████████████████████");
        terminal.println("████  █████ ███████████ ██████████ █████████                      ██████████████");
        terminal.println("███   █████ ..          ██████████ █████████  ██████  ██████████  ██████████████");
        terminal.println("███   ████████ ███████████████████ █████████ ███████ ████████████ ██████████████");
        terminal.println("███  █████████ ███████████████████ █████████ ███████ ████████████ ██████████████");
        terminal.println("███   ███████   █████████████████   ███████   █████   ██████████  ██████████████");
        terminal.println("███K                                      K .       .            .██████████████");
        terminal.println("████████████████████████████████████████████████████████████████████████████████");
    }

    public void level7() {
        terminal.clearScreen();
        lo = "@";
        gewon7 = 10;
        ax = 1;
        ay = 7;

        setForegroundColor(14);
        terminal.println("    KROZ");
        terminal.println("");
        setForegroundColor(12);
        terminal.println(" 0  Gefressene PUNKTE (von 10)                            (Q)uit            ");
        terminal.println(" 0  Verbleibende KACKIS");
        terminal.println("");
        setForegroundColor(10);

        terminal.println("████████████████████████████████████████████████████████████████████████████████");
        terminal.println("█                                                                              █");
        terminal.setPos(1, 6);
        this.setForegroundColor(0);
        terminal.setLocalBackgroundColor(13 + 14);
        terminal.println(" Kroz muss auch diese 10 superboese, streng geheime Punkte besiegen!!         ");
        terminal.setLocalBackgroundColor(0);

        this.setForegroundColor(10);
        terminal.println("█ ██████████████████████████████████████████████████████████████████████████████");
        terminal.println("█                                                                              █");
        terminal.setPos(40, 8);
        this.setForegroundColor(11);
        terminal.println("K");
        this.setForegroundColor(10);

        terminal.println("██████████████████████████████████████████████████████████████████████████████@█");
        terminal.println("█                                                                              █");
        terminal.println("█@██████████████████████████████████████████████████████████████████████████████");
        terminal.println("█                                                                              █");
        terminal.println("██████████████████████████████████████████████████████████████████████████████@█");
        terminal.println("█                                                                              █");
        terminal.println("█@██████████████████████████████████████████████████████████████████████████████");
        terminal.println("█                                                                              █");
        terminal.println("██████████████████████████████████████████████████████████████████████████████@█");
        terminal.println("█                                                                              █");
        terminal.println("█@██████████████████████████████████████████████████████████████████████████████");
        terminal.println("█                                                                              █");
        terminal.println("██████████████████████████████████████████████████████████████████████████████@█");
        terminal.println("█                                                                              █");
        terminal.println("████████████████████████████████████████████████████████████████████████████████");
        for (int i = 0; i < 5; i++) {
            this.setForegroundColor(9 + (int) (Math.random() * 6));
            terminal.setPos(10 + (int) (Math.random() * 25), i * 2 + 14);
            terminal.println(".");

            this.setForegroundColor(9 + (int) (Math.random() * 6));
            terminal.setPos(38 + (int) (Math.random() * 25), i * 2 + 14);
            terminal.println(".");
        }

    }

    public void mail() {
        terminal.clearScreen();
        setForegroundColor(4);
        terminal.println("Mail:");
        terminal.println("");
        terminal.println("");
        terminal.println("");
        terminal.println("                             Schmidt3000@gmx.de");
        terminal.println("");
        terminal.println("");
        setForegroundColor(15);
        terminal.println("");
        terminal.println("");
        terminal.println("                            www.schmidt3000.de.vu ");
        terminal.println("");
        terminal.println("");
        terminal.println("");
        terminal.println("");
        terminal.println("");
        terminal.println("");
        terminal.println("");
        setForegroundColor(13);
        terminal.println("                   'QBASIC -> JAVA' PORTIERUNG 29-2-2002 ");
        terminal.println("                              BY ANDRE D. FISCHER");
        setForegroundColor(15);
        terminal.println("                             Rita.Suessmuth@gmx.de");
        terminal.println("");
        setForegroundColor(14);
        terminal.println("");

        terminal.println("");
        terminal.println("");
        terminal.println("                    Beliebige Taste zum Fortfahren !!!");
        terminal.sleep();

        // GOTO s:
    }

    private void menue() {
        while (true) {
            titel();
            setForegroundColor(3);
            terminal.println("(S)PIELEN (P)ASSWORT (A)NLEITUNG (M)AIL ");
            String text = "                                                  BY MARTIN SCHMIDT            Special Thanks to ANDRE D. FISCHER            QBASIC -> JAVA PORT BY ANDRE D. FISCHER                       pwd=007  what ever that means....                                ";
            int sw = 20;
            int pos = 0;
            boolean notSelected = true;
            while (notSelected) {
                int key = 0;
                // while(key==0)
                key = terminal.read();
                String ts = "" + (char) key;
                setPos(sw, 20);
                setForegroundColor(10);
                terminal.println(text.substring(pos, pos + 40));
                pos = pos + 1;
                if (pos == text.length() - 40) {
                    pos = 0;
                }

                notSelected = false;
                if (ts.toLowerCase().equals("s")) {
                    level = 1;
                    anfang();

                } else if (ts.toLowerCase().equals("a")) {
                    anleitung();
                } else if (ts.toLowerCase().equals("m")) {
                    mail();
                } else if (ts.toLowerCase().equals("q")) {
                    ende();
                } else if (ts.toLowerCase().equals("p")) {
                    if (pass())
                        anfang();
                } else {
                    notSelected = true;
                }
                // IF ts$ = "a" THEN GOTO anleitung:
                // IF ts$ = "m" THEN GOTO mail:
                // IF ts$ = "q" THEN GOTO ende:
                // IF ts$ = "p" THEN GOTO pass:

            }
        }

    }

    public boolean pass() {
        while (true) {
            terminal.clearScreen();
            setForegroundColor(4);
            terminal.println("");
            terminal.println("Mit der Passwort-Option kannst du an einem vorherigen Spiel weiterspielen.");
            terminal.println("Wenn du kein Passwort eingeben willst druecke ENTER um zurueck zum Anfangs-");
            terminal.println("bildschirm zu kommen !");
            terminal.println("");
            setForegroundColor(10);
            terminal.println("Wie lautet das PASSWORT ");
            // passloop:

            setPos(7, 0);
            String pass = "" + terminal.readln();

            if (pass.equals("0")) {
            }
            if (pass.equals("111")) {
                level = 1;
                break;
            }
            if (pass.equals("126")) {
                level = 2;
                break;
            }
            if (pass.equals("359")) {
                level = 3;
                break;
            }
            if (pass.equals("533")) {
                level = 4;
                break;
            }
            if (pass.equals("753")) {
                level = 5;
                break;
            }
            if (pass.equals("899")) {
                level = 6;
                break;
            }
            if (pass.equals("007")) {
                level = 7;
                break;
            }

            if (pass.equals("")) {
                return false;
            }

        }
        return true;

    }

    public void punkt() {

        // SOUND 100, 1
        punkte = punkte + 1;
        setPos(1, 8);
        // terminal.println ("");
        terminal.println("");
        String p = (" " + punkte);
        boolean won = false;
        // p =p.substring(p.length()-2,p.length());
        terminal.println("" + p);
        if (punkte == gewon && level == 1) {
            ziel("126", 2, "ersten");
            level = 2;
            won = true;
        } else if (punkte == gewon2 && level == 2) {
            ziel("359", 3, "zweiten");
            level = 3;
            won = true;
        } else if (punkte == gewon3 && level == 3) {
            ziel("533", 4, "dritten");
            level = 4;
            won = true;
        } else if (punkte == gewon4 && level == 4) {
            ziel("735", 5, "vierten");
            level = 5;
            won = true;
        } else if (punkte == gewon5 && level == 5) {
            ziel("899", 6, "fuenften");
            level = 6;
            won = true;
        } else if (punkte == gewon6 && level == 6) {
            finale();
            level = 1;
            won = true;

        } else if (punkte == gewon7 && level == 7) {
            ziel("keins noetig!", 1, "geheimen");
            level = 1;
            won = true;
        }
        if (won) {
            setLevel();
            won = false;
        }
        // GOTO schritt:

    }

    public void schritt(int x, int y) {

        setForegroundColor(4);
        setPos(ay, ax);
        terminal.println("" + lo);
        ax = ax + x;
        ay = ay + y;
        setPos(ay, ax);
        terminal.println("" + (char) 2);
        setForegroundColor(14);

    }

    private void setForegroundColor(int col) {

        terminal.setForegroundColor(col + 13);

    }

    private void setLevel() {
        punkte = 0;
        this.kackp = -1;
        this.pam = 0;

        switch (level) {
            case 1:
                this.level1();
                break;

            case 2:
                this.level2();
                break;

            case 3:
                this.level3();
                break;

            case 4:
                this.level4();
                break;

            case 5:
                this.level5();
                break;

            case 6:
                this.level6();
                break;

            case 7:
                this.level7();
                break;

        }
        schritt(0, 0);

    }

    private void setPos(int y, int x) {
        terminal.setPos(x, y);
    }

    public void titel() {
        terminal.clearScreen();
        setForegroundColor(14);
        terminal.println("");
        terminal.println("");
        terminal.println("                ████   ████  ▓▓▓▓▓▓▓▓▓▓    ▒▒▒▒▒▒▒▒   ░░░░░░░░░ ");
        terminal.println("               █████  ████   ▓▓▓▓    ▓▓▓  ▒▒▒▒▒▒▒▒▒▒       ░░░░░");
        terminal.println("               ██████████    ▓▓▓▓    ▓▓▓  ▒▒▒    ▒▒▒      ░░░░░ ");
        terminal.println("               ████████      ▓▓▓▓▓▓▓▓▓▓   ▒▒      ▒▒    ░░░░░░  ");
        terminal.println("               ██████████    ▓▓▓▓▓▓▓▓     ▒▒▒    ▒▒▒   ░░░░░░   ");
        terminal.println("               █████  ████   ▓▓▓▓  ▓▓▓▓   ▒▒▒▒▒▒▒▒▒▒  ░░░░░░    ");
        terminal.println("                ████  █████  ▓▓▓▓   ▓▓▓▓   ▒▒▒▒▒▒▒▒    ░░░░░░░░░");
        terminal.println("");
        setForegroundColor(12);
        terminal.println("                                                      28-02-2002");
        terminal.println("");
        terminal.println("");
        setForegroundColor(13);
        terminal.println("                           THE ADVENTURES OF KROZ");
        setForegroundColor(5);
        terminal.println("                                   PART I");
        terminal.println("");
        terminal.println("");
        terminal.println("");
        terminal.println("");
        terminal.println("");
        terminal.println("");
        terminal.println("");
    }

    public void ziel(String passw, int level, String zahlw) {
        terminal.clearScreen();
        // CLEAR
        setForegroundColor(2);
        terminal.println("");
        terminal.println("                                    GEWONNEN");
        terminal.println("");
        terminal.println("                       Du hast den " + zahlw + " Level bestanden.");
        terminal.println("");
        terminal.println("");
        setForegroundColor(7);
        terminal.println("            Dein PASSWORT um in LEVEL " + level + " zu beginnen lautet:");
        terminal.println("");
        terminal.println("");
        setForegroundColor(15);
        terminal.println("            " + passw);
        setForegroundColor(14);
        terminal.println("");
        terminal.println("");
        terminal.println("                       (Beliebige Taste zum Fortfahren)");

        terminal.sleep();

        // GOTO level2:
    }
}