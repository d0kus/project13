package factory;

import repository.IJoblistingRepository;
import repository.IPortalRepository;
import repository.JoblistingRepository;
import repository.PortalRepository;

public final class RepositoryFactory {
    private RepositoryFactory() {}

    public static IPortalRepository portalRepository() {
        return new PortalRepository();
    }

    public static IJoblistingRepository joblistingRepository() {
        return new JoblistingRepository();
    }
}