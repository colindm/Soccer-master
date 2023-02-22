package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.Hashtable;
// for files
import java.io.FileInputStream;

/**
 * Soccer player database -- presently, all dummied up
 *
 * @author *** put your name here ***
 * @version *** put date of completion here ***
 *
 */
public class SoccerDatabase implements SoccerDB {

    // dummied up variable; you will need to change this
//    private Hashtable database;

    Hashtable<String, SoccerPlayer> database = new Hashtable<>();

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */

    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {
        SoccerPlayer player = new SoccerPlayer(firstName, lastName, uniformNumber, teamName);
//        check if player already exists
        if (database.containsKey(firstName + "##" + lastName)) {
            Log.i("add player", "player already exists");
            return false;
        }
        database.put(firstName + "##" + lastName, player);
        Log.i("add player", firstName + " " + lastName + " " + uniformNumber + " " + teamName);
//        print hashtable
        Log.i("database", database.toString());

        return true;
    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {
//        check if player exists
        if (database.containsKey(firstName + "##" + lastName)) {
            Log.i("remove player", firstName + " " + lastName);
            database.remove(firstName + "##" + lastName);
            return true;
        }

        return false;
    }

    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {
        if (database.containsKey(firstName + "##" + lastName)) {
            Log.i("get player", firstName + " " + lastName);
            return database.get(firstName + "##" + lastName);
        }

        return null;
    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {
//        add goal to player
        if (database.containsKey(firstName + "##" + lastName)) {
            Log.i("bump goals", firstName + " " + lastName);
            database.get(firstName + "##" + lastName).bumpGoals();
            return true;
        }
        return false;
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {
//        add yellow card to player
        if (database.containsKey(firstName + "##" + lastName)) {
            Log.i("bump yellow cards", firstName + " " + lastName);
            database.get(firstName + "##" + lastName).bumpYellowCards();
            return true;
        }
        return false;
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {
//        add red card to player
        if (database.containsKey(firstName + "##" + lastName)) {
            Log.i("bump red cards", firstName + " " + lastName);
            database.get(firstName + "##" + lastName).bumpRedCards();
            return true;
        }
        return false;
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {
// if team name is null, return size of database
        if (teamName == null) {
            Log.i("num players", "all players");
            return database.size();
        }
//        if team name is not null, return size of team
        else {
            Log.i("num players", teamName);
            int count = 0;
            for (String key : database.keySet()) {
                if (database.get(key).getTeamName().equals(teamName)) {
                    count++;
                }
            }
            return count;
        }
//        return -1;
    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int idx, String teamName) {
//       if team name is null, return nth player in database
        if (teamName == null) {
            Log.i("player index", "all players");
            int count = 0;
            for (String key : database.keySet()) {
                if (count == idx) {
                    return database.get(key);
                }
                count++;
            }
        } else {
//            if team name is not null, return nth player on team
            Log.i("player index", teamName);
            int count = 0;
            for (String key : database.keySet()) {
                if (database.get(key).getTeamName().equals(teamName)) {
                    if (count == idx) {
                        return database.get(key);
                    }
                    count++;
                }
            }
        }
        return null;
    }

    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @Override
    public boolean readData(File file) {
//        if file exists, read data from file
        if (file.exists()) {
            Log.i("read data", "file exists");
            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split("##");
                    for (int i = 0; i < data.length; i++) {
                        Log.i("read data", data[i]);
                    }
                    SoccerPlayer player = new SoccerPlayer(data[0], data[1], Integer.parseInt(data[2]), data[3]);
                    Log.i("read data player", player.toString());
                    database.put(data[0] + "##" + data[1], player);
                }
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.exists();
    }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @Override
    public boolean writeData(File file) {
//        create file if it doesn't exist
        if (!file.exists()) {
            try {
                file.createNewFile();
                Log.i("write data", "file created");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        if file exists, write data to file
        if (file.exists()) {
            Log.i("write data", "file exists");
            try {
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);
                for (String key : database.keySet()) {
                    bw.write(database.get(key).toString());
                    bw.newLine();
                }
                bw.close();
                fw.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
        Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {
        return new HashSet<String>();
    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}
