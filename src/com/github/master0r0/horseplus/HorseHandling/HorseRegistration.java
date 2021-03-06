package com.github.master0r0.horseplus.HorseHandling;

import com.github.master0r0.horseplus.HorsePlus;
import com.github.master0r0.horseplus.PlayerHandling.PlayerRegistration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Licensed under the GNU Public License
 * <p/>
 * Created by Master0r0 on 04/03/2016.
 */
public class HorseRegistration {
    private static HorsePlus plugin = HorsePlus.getPlugin(HorsePlus.class);

    public static boolean RegisterHorse(Horse horse){
        Player owner = (Player) horse.getOwner();
        String HorseUUID = String.valueOf(horse.getUniqueId());
        String HorseName = horse.getName();

        //Creates a directory for files to be stored in
        File horsedataDir = new File(plugin.getDataFolder(),File.separator+"Horse Data");
        //Creates a File variable with the path of HorseDataDir(Where the config is found) and the Horses UUID with the .yml extension
        File horsedata = new File(horsedataDir,File.separator+HorseUUID+".yml");
        //Generates a file configuration in the form of a yml file of File f
        FileConfiguration horseData = YamlConfiguration.loadConfiguration(horsedata);

        if(!horsedata.exists()){
            try{
                Map<String,Player> riders = new HashMap<String, Player>();
                ConfigurationSection horseInfo = horseData.createSection("Horse Info");
                ConfigurationSection ownerInfo = horseData.createSection("Owner Info");

                //Horse Info data
                horseInfo.set("UUID",HorseUUID);
                horseInfo.set("Name",HorseName);
                horseInfo.set("PluginID",String.valueOf(generateHorseID(horse)));

                //Owner Info data
                ownerInfo.set("Name",owner.getDisplayName());
                ownerInfo.set("Riders", riders);

                //Attempts to save the data made above in File f
                horseData.save(horsedata);
            }catch(IOException exception) {
                exception.printStackTrace();
                return false;
            }
            return true;
        }else{
            return false;
        }
    }

    public static int generateHorseID(Horse horse){
        int max = 1000;
        int min = 1;

        int randomID = min + (int)(Math.random()*(max-min)+1);

        if(getHorseUUIDFromID(String.valueOf(randomID))==null||getHorseUUIDFromID(String.valueOf(randomID)).equalsIgnoreCase("")) {
            storeHorseID(horse,randomID);
            return randomID;
        }
        return 0;
    }

    public static void storeHorseID(Horse horse, int ID){
        String HorseUUID = String.valueOf(horse.getUniqueId());

        //Creates a directory for files to be stored in
        File horsedataDir = new File(plugin.getDataFolder(),File.separator+"Horse Data");
        //Creates a File variable with the path of HorseDataDir(Where the config is found) and the Horses UUID with the .yml extension
        File horsedata = new File(horsedataDir,File.separator+"HorseIDs.yml");
        //Generates a file configuration in the form of a yml file of File f
        FileConfiguration horseData = YamlConfiguration.loadConfiguration(horsedata);

        if(!horsedata.exists()){
            try{
                ConfigurationSection horseID = horseData.createSection("Horse ID's");
                horseID.set(String.valueOf(ID),String.valueOf(HorseUUID));

                horseData.save(horsedata);
            }catch(IOException exception) {
                exception.printStackTrace();
            }
        }else{
            try{
                ConfigurationSection horseID = horseData.getConfigurationSection("Horse ID's");
                horseID.set(String.valueOf(ID),String.valueOf(HorseUUID));

                horseData.save(horsedata);
            }catch(IOException exception) {
                exception.printStackTrace();
            }
        }

    }

    public static void unregisterHorse(String HorseUUID, String OwnerName){
        //Creates a directory for files to be stored in
        File horsedataDir = new File(plugin.getDataFolder(),File.separator+"Horse Data");
        //Creates a File variable with the path of HorseDataDir(Where the config is found) and the Horses UUID with the .yml extension
        File horsedata = new File(horsedataDir,File.separator+HorseUUID+".yml");
        FileConfiguration horseData = YamlConfiguration.loadConfiguration(horsedata);

        File horseiddata = new File(horsedataDir,File.separator+"HorseIDs.yml");
        FileConfiguration horseIDData = YamlConfiguration.loadConfiguration(horseiddata);

        File playerDataDir = new File(plugin.getDataFolder(),File.separator+"Player Data");
        File playerdata = new File(playerDataDir,File.separator+OwnerName+".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerdata);
        FileConfiguration newPlayerData = YamlConfiguration.loadConfiguration(playerdata);

        ConfigurationSection horseID = horseData.getConfigurationSection("Horse Info");

        if(horseiddata.exists()){
            try{
                ConfigurationSection horseIDList = horseIDData.getConfigurationSection("Horse ID's");
                horseIDList.set(horseID.getString("PluginID"),"");
                horseIDData.save(horseiddata);
            }catch(IOException exception){
                exception.printStackTrace();
            }
        }

        if(playerdata.exists()){
            try {
                ConfigurationSection ownedHorses = playerData.getConfigurationSection("Owned Horses");
                ownedHorses.set(horseID.getString("PluginID"), "");

                playerData.save(playerdata);
            }catch(IOException exception){
                exception.printStackTrace();
            }
        }

        if(playerdata.exists()){
            try{
                ConfigurationSection playerInfo = playerData.getConfigurationSection("Player Info");

                ConfigurationSection newPlayerInfo = newPlayerData.createSection("Player Info");
                ConfigurationSection newOwnedHorses = newPlayerData.createSection("Owned Horses");

                Map<String,Object> horses = PlayerRegistration.getHorses(OwnerName);
                if (horses != null) {
                    Iterator it = horses.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        if(!(String.valueOf(horses.get(pair.getKey())).equalsIgnoreCase("")))
                            newOwnedHorses.set(String.valueOf(pair.getKey()),horses.get(pair.getKey()));
                        it.remove();
                    }
                }

                Map<String,Object> oldPlayerData = playerInfo.getValues(true);
                Iterator it = oldPlayerData.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry pair = (Map.Entry) it.next();
                    newPlayerInfo.set(String.valueOf(pair.getKey()),oldPlayerData.get(pair.getKey()));
                }

                newPlayerData.save(playerdata);
            }catch(IOException exception){
                exception.printStackTrace();
            }
        }

        if(horsedata.exists()) {
            horsedata.delete();
        }
    }

    public static int getHorseID(Horse horse){
        String HorseUUID = String.valueOf(horse.getUniqueId());

        //Creates a directory for files to be stored in
        File horsedataDir = new File(plugin.getDataFolder(),File.separator+"Horse Data");
        //Creates a File variable with the path of HorseDataDir(Where the config is found) and the Horses UUID with the .yml extension
        File horsedata = new File(horsedataDir,File.separator+"HorseIDs.yml");
        //Generates a file configuration in the form of a yml file of File f
        FileConfiguration horseData = YamlConfiguration.loadConfiguration(horsedata);

        if(horsedata.exists()){
            ConfigurationSection horseID = horseData.getConfigurationSection("Horse ID's");
            return horseID.getInt(HorseUUID);
        }else{
            plugin.logger.info("Horse ID File was not found!");
        }
        return 0;
    }

    public static String getHorseUUIDFromID(String ID){
        //String HorseUUID = String.valueOf(horse.getUniqueId());

        //Creates a directory for files to be stored in
        File horsedataDir = new File(plugin.getDataFolder(),File.separator+"Horse Data");
        //Creates a File variable with the path of HorseDataDir(Where the config is found) and the Horses UUID with the .yml extension
        File horsedata = new File(horsedataDir,File.separator+"HorseIDs.yml");
        //Generates a file configuration in the form of a yml file of File f
        FileConfiguration horseData = YamlConfiguration.loadConfiguration(horsedata);

        if(horsedata.exists()){
            ConfigurationSection horseID = horseData.getConfigurationSection("Horse ID's");
            return horseID.getString(ID);
        }else{
            plugin.logger.info("Horse ID File was not found!");
        }
        return null;
    }

}
