package org.infinispan.samplemodule.api;

import org.infinispan.Cache;
import org.infinispan.context.InvocationContextContainer;
import org.infinispan.factories.ComponentRegistry;
import org.infinispan.interceptors.InterceptorChain;
import org.infinispan.samplemodule.CommandInitializer;
import org.infinispan.samplemodule.commands.nonvisitable.PerformGCCommand;
import org.infinispan.samplemodule.commands.visitable.BulkDeleteCommand;
import org.infinispan.samplemodule.commands.visitable.PrintContentsCommand;
import org.infinispan.samplemodule.interceptors.SampleModuleInterceptor;

/**
 * A sample decorator adding behaviour to Infinispan.  This could typically be the module's "API", if it has one.
 *
 * @author Manik Surtani
 * @since 5.0
 */
public class SampleModuleDecorator<K, V> {
   private final Cache<K, V> cache;
   private final CommandInitializer commandBuilder;
   private final InterceptorChain invoker;
   private final InvocationContextContainer invocationContextContainer;

   public SampleModuleDecorator(Cache<K, V> cache) {
      if (!cache.getStatus().allowInvocations())
         throw new IllegalStateException("Cache instance not running!");
      this.cache = cache;
      ComponentRegistry cr = cache.getAdvancedCache().getComponentRegistry();
      commandBuilder = cr.getComponent(CommandInitializer.class);
      invoker = cr.getComponent(InterceptorChain.class);
      invocationContextContainer = cr.getComponent(InvocationContextContainer.class);

      // we may want to attach the interceptor to the cache.
      // alternatively the module may register for lifecycle callbacks for each new cache started up, by implementing
      // org.infinispan.lifecycle.ModuleLifecycle

      SampleModuleInterceptor i = new SampleModuleInterceptor();
      cr.registerComponent(i, SampleModuleInterceptor.class);
      cache.getAdvancedCache().addInterceptor(i, 1);
   }

   public Cache<K, V> getCache() {
      return cache;
   }

   /**
    * This only happens remotely; not on the local node at all.
    */
   public void performRemoteGCOnCluster() {
      PerformGCCommand c = commandBuilder.buildPerformGCCommand();
      cache.getAdvancedCache().getRpcManager().broadcastRpcCommand(c, false);
   }

   public void bulkDelete(String pattern) {
      if (pattern == null || pattern.length() == 0) throw new IllegalArgumentException("Pattern cannot be null or blank");
      BulkDeleteCommand c = commandBuilder.buildBulkDeleteCommand(pattern);
      // The number of entries is not limited to 10, but this
      // guarantees a decent size to initialise the context with
      invoker.invoke(invocationContextContainer.createInvocationContext(true, 10), c);
   }

   public void printCacheContents() {
      PrintContentsCommand c = commandBuilder.buildPrintContentsCommand();
      invoker.invoke(invocationContextContainer.createInvocationContext(false, 0), c);
   }

}
