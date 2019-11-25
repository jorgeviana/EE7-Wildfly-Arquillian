package inttest;

import org.jboss.arquillian.core.spi.LoadableExtension;

public class AutoDiscoverInstanceExtension implements LoadableExtension {
	@Override
	public void register(ExtensionBuilder builder) {
		builder.observer(LoadContainerConfiguration.class);
	}
}
