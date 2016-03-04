package com.github.master0r0.horseplus.HorseHandling;

import com.github.master0r0.horseplus.HorsePlus;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Horse;

import java.io.File;
import java.io.IOException;

/**
 * Licensed under the GNU Public License
 * <p/>
 * Created by Master0r0 on 04/03/2016.
 */
public class CheckHorse {

    private HorsePlus plugin = HorsePlus.getPlugin(HorsePlus.class);

    public CheckHorse(Horse horse){

        String HorseUUID = String.valueOf(horse.getUniqueId());
        String HorseName = horse.getName();

        //Creates a directory for files to be stored in
        File horsedataDir = new File(plugin.getDataFolder(),File.separator+"Horse Data");
        //Creates a File variable with the path of HorseDataDir(Where the config is found) and the Horses UUID with the .yml extension
        File horsedata = new File(horsedataDir,File.separator+HorseUUID+".yml");
        //Generates a file configuration in the form of a yml file of File f
        FileConfiguration horseData = YamlConfiguration.loadConfiguration(horsedata);

        if(horsedata.exists()){
            try{
                ConfigurationSection horseInfo = horseData.getConfigurationSection("Horse Info");
                ConfigurationSection ownerInfo = horseData.getConfigurationSection("Owner Info");

                //Horse Info data
                if(horseInfo.get("Name")!=HorseName)
                    horseInfo.set("Name",HorseName);

                //Owner Info data


                //Attempts to save the data made above in File f
                horseData.save(horsedata);
            }catch(IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}