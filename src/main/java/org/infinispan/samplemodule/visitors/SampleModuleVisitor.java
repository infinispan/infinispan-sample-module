package org.infinispan.samplemodule.visitors;

import org.infinispan.commands.Visitor;
import org.infinispan.context.InvocationContext;
import org.infinispan.samplemodule.commands.visitable.BulkDeleteCommand;
import org.infinispan.samplemodule.commands.visitable.PrintContentsCommand;

/**
 * A visitor defining the ability to visit new commands defined in this module.
 *
 * @author Manik Surtani
 * @since 5.0
 */
public interface SampleModuleVisitor extends Visitor {

   Object visitPrintContentsCommand(InvocationContext ctx, PrintContentsCommand command) throws Throwable;

   Object visitBulkDeleteCommand(InvocationContext ctx, BulkDeleteCommand command) throws Throwable;

}
