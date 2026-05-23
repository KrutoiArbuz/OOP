package ru.nsu.masolygin.solutions;

public class DistributedPrimeChecker implements PrimeChecker {

    @Override
    public boolean containsComposite(int[] arr) {
        for (int num : arr) {
            if (!PrimeChecker.isPrime(num)) {
                return true;
            }
        }
        return false;
    }
}
