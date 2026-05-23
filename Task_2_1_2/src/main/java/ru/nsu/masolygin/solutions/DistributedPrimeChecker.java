package ru.nsu.masolygin.solutions;

import java.net.InetSocketAddress;
import java.util.List;
import ru.nsu.masolygin.master.Master;

public class DistributedPrimeChecker implements PrimeChecker {

    private final List<InetSocketAddress> workers;
    private final int chunkSize;

    public DistributedPrimeChecker(List<InetSocketAddress> workers, int chunkSize) {
        this.workers = workers;
        this.chunkSize = chunkSize;
    }

    @Override
    public boolean containsComposite(int[] arr) {
        return new Master(workers, chunkSize).containsComposite(arr);
    }
}
