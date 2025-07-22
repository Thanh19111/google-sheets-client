package org.thanhpham.util;

import java.util.HashSet;
import java.util.Set;

public class ProcessManager {
    private static final Set<Integer> processingSet = new HashSet<>();
    private static boolean isBlocked = false;

    public static void setStatus(boolean inactive){
        isBlocked = inactive;
    }

    public static boolean getStatus(){
        return isBlocked;
    }

    public static synchronized boolean tryProcess(int position) {
        if (isBlocked || processingSet.contains(position)) return false;
        processingSet.add(position);
        return true;
    }

    public static synchronized void finishProcess(int position) {
        processingSet.remove(position);
    }

    public static class ProcessingLock implements AutoCloseable {
        private final int position;

        private ProcessingLock(int position) {
            this.position = position;
        }

        @Override
        public void close() {
            finishProcess(position);
        }
    }

    public static ProcessingLock waitForLock(int position) {
        while (!tryProcess(position)) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
        return new ProcessingLock(position);
    }
}
