package org.infinispan.samplemodule;

import org.infinispan.Cache;
import org.infinispan.config.Configuration;
import org.infinispan.config.GlobalConfiguration;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.samplemodule.api.SampleModuleDecorator;
import org.testng.annotations.Test;

@Test
public class SampleUsageTest {

   public void demonstrateUsage() {
      Configuration cfg = new Configuration();
      cfg.setCacheMode(Configuration.CacheMode.REPL_SYNC);

      EmbeddedCacheManager manager1 = new DefaultCacheManager(GlobalConfiguration.getClusteredDefault(), cfg);
      Cache<String, String> cache1 = manager1.getCache();

      EmbeddedCacheManager manager2 = new DefaultCacheManager(GlobalConfiguration.getClusteredDefault(), cfg);
      Cache<String, String> cache2 = manager2.getCache();

      SampleModuleDecorator<String, String> moduleApi1 = new SampleModuleDecorator<String, String>(cache1);
      SampleModuleDecorator<String, String> moduleApi2 = new SampleModuleDecorator<String, String>(cache2);

      cache1.put("1", "test");
      cache1.put("2", "test");
      cache1.put("3", "test");
      cache1.put("FOUR", "test");

      moduleApi1.performRemoteGCOnCluster();
      moduleApi2.printCacheContents();

      moduleApi1.bulkDelete("[0-9]*");
      moduleApi2.printCacheContents();
   }
}
