package org.infinispan.samplemodule;

import org.infinispan.factories.components.ModuleMetadataFileFinder;

/**
 * Metadata file finder
 *
 * @author Galder Zamarreño
 * @since 5.1
 */
public class MetadataFileFinder implements ModuleMetadataFileFinder {

   @Override
   public String getMetadataFilename() {
      return "infinispan-sample-module-component-metadata.dat";
   }

}
