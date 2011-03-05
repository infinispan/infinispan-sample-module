package org.infinispan.samplemodule.interceptors;

import org.infinispan.commands.write.ClearCommand;
import org.infinispan.context.InvocationContext;
import org.infinispan.factories.annotations.Inject;
import org.infinispan.interceptors.base.CommandInterceptor;
import org.infinispan.remoting.rpc.RpcManager;
import org.infinispan.samplemodule.commands.visitable.BulkDeleteCommand;
import org.infinispan.samplemodule.commands.visitable.PrintContentsCommand;
import org.infinispan.samplemodule.visitors.SampleModuleVisitor;

/**
 * An interceptor for this module, if this module needs to add behaviour to the cache.
 *
 * @author Manik Surtani
 * @since 5.0
 */
public class SampleModuleInterceptor extends CommandInterceptor implements SampleModuleVisitor {
   RpcManager rpcManager;

   @Inject
   public void injectDependencies(RpcManager rpcManager) {
      this.rpcManager = rpcManager;
   }

   @Override
   public Object visitPrintContentsCommand(InvocationContext ctx, PrintContentsCommand command) throws Throwable {
      Object o = super.invokeNextInterceptor(ctx, command);
      log.info("Cache contents are %s", o);
      return o;
   }

   @Override
   public Object visitBulkDeleteCommand(InvocationContext ctx, BulkDeleteCommand command) throws Throwable {
      log.debug("Bulk-deleting String keys that match regular expression %s", command.getPattern());

      if (ctx.isOriginLocal()) {
         // Note that the usual replication interceptor will not handle this new command!  So we'd need to replicate it here if needed.
         rpcManager.broadcastRpcCommand(command, true);
      }

      return super.invokeNextInterceptor(ctx, command);
   }

   @Override
   public Object visitClearCommand(InvocationContext ctx, ClearCommand command) throws Throwable {
      // Let's prohibit the use of cache.clear() in this module.
      throw new UnsupportedOperationException("Cache.clear() not supported if this module is used.");
   }
}
