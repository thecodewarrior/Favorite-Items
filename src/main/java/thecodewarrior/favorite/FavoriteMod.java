package thecodewarrior.favorite;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

@Mod(modid=FavoriteMod.MODID, name=FavoriteMod.MODNAME, version=FavoriteMod.VERSION, acceptableRemoteVersions = "*")
public class FavoriteMod {
	public static final String MODID = "favorites";
	public static final String MODNAME = "Favorites!";
	public static final String VERSION = "0.0.0";
	public static final Logger l = LogManager.getLogger("Favorites");
	
	@SidedProxy(serverSide="thecodewarrior.favorite.CommonProxy", clientSide="thecodewarrior.favorite.ClientProxy")
	public static CommonProxy proxy;
	
	public static SimpleNetworkWrapper network;
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event) {
		network = NetworkRegistry.INSTANCE.newSimpleChannel("FavoriteChannel");
	    network.registerMessage(MessageSetFavorite.Handler.class, MessageSetFavorite.class, 0, Side.SERVER);
	    MinecraftForge.EVENT_BUS.register(proxy);
	    proxy.init();
	    
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        
        ASMHooks.completeLock = config.getBoolean("complete_lock", Configuration.CATEGORY_GENERAL, false, "Don't let favorited items be moved by picking them up.");
        
        config.save();
	}
}