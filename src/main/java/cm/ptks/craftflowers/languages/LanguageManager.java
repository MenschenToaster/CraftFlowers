package cm.ptks.craftflowers.languages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import com.fastasyncworldedit.core.configuration.file.YamlConfiguration;

import cm.ptks.craftflowers.CraftFlowers;

public class LanguageManager {
    
    private final CraftFlowers plugin;
    private final List<Language> languages = new ArrayList<>();

    private Language defaultLanguage;

    public LanguageManager(CraftFlowers plugin) {
        this.plugin = plugin;
    
        String defaultLanguage = plugin.getConfig().getString("language.default");

        for(String language : plugin.getConfig().getStringList("language.list")) {
            File languageFile = new File(plugin.getDataFolder(), "lang/" + language + ".yml");

            if(!languageFile.exists()) {
                InputStream inputStream = plugin.getResource("lang/" + language + ".yml");
                if(inputStream == null) {
                    plugin.getLogger().warning("Failed to load or copy " + language + "...");
                    continue;
                }
                languageFile.getParentFile().mkdirs();
                
                try {
                    languageFile.createNewFile();
                } catch (IOException e) {
                    plugin.getLogger().log(Level.SEVERE, "Failed to create language file for " + language + "...", e);
                    continue;
                }
                try (FileOutputStream os = new FileOutputStream(languageFile)) {
                    os.write(inputStream.readAllBytes());
                    inputStream.close();
                } catch (IOException e) {
                    plugin.getLogger().log(Level.SEVERE, "Failed to load or copy " + language + "...", e);
                    continue;
                }
            }

            updateLanguageFile(languageFile, language);
            updateLanguageFile(languageFile, "en_US");

            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(languageFile);
            Language lang = new Language(Language.parseKeys(configuration), language);
            if(defaultLanguage.equals(language)) {
                this.defaultLanguage = lang;
            }
            this.languages.add(lang);
        }
        
        for(String key : plugin.getConfig().getConfigurationSection("language.mapping").getKeys(false)) {
            Language language = getLanguage(key);
            if(language == null)
                throw new IllegalStateException("Language not found " + key  + " to map to");
            
            language.addAliases(plugin.getConfig().getStringList("language.mapping." + key));
        }

        if(this.defaultLanguage == null) {
            this.defaultLanguage = new Language(new HashMap<>(), defaultLanguage);
        }
    }

    private void updateLanguageFile(File languageFile, String languageCode) {
        //Copy new language keys over to the file
        InputStream resource = plugin.getResource("lang/" + languageCode + ".yml");
        if(resource != null) {
            try {
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(languageFile);
                configuration.addDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(resource, StandardCharsets.UTF_8)));
                configuration.options().copyDefaults(true);
                configuration.save(languageFile);
                resource.close();
            } catch(Exception e) {
                //ignore
            }
        }
    }

    public Language getDefaultLanguage() {
        return defaultLanguage;
    }

    public Language getLanguage(Player player) {
        if(player == null)
            return defaultLanguage;
        
        for(Language language : this.languages) {
            if(language.isLanguage(player.getLocale())) {
                return language;
            }
        } 
        return defaultLanguage;
    }


    public Language getLanguage(String locale) {
    
        for(Language language : this.languages) {
            if(language.isLanguage(locale)) {
                return language;
            }
        } 
        return defaultLanguage;
    }


}
