package by.org.cgm.didyoufeelit;

import by.org.cgm.didyoufeelit.models.RegisteredUser;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: Anatol Salanevich
 * Date: 11.05.2015
 */
public class AppCache {
    
    private static AppCache mAppCache;
    @Getter @Setter private RegisteredUser mUser;
    
    public static AppCache getInstance() {
        if (mAppCache == null) mAppCache = new AppCache();
        return mAppCache;
    }

}
