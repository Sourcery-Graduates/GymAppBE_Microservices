package com.sourcery.gymapp.email.config;

public class KafkaProcessingContext {
    private static final ThreadLocal<Boolean> isKafkaProcessing = ThreadLocal.withInitial(() -> false);

    /**
     * Enables Kafka processing state.
     */
    public static void enableKafkaProcessing() {
        isKafkaProcessing.set(true);
    }

    /**
     * Disables Kafka processing state.
     */
    public static void disableKafkaProcessing() {
        isKafkaProcessing.remove();
    }

    /**
     * Retrieves the current Kafka processing state.
     *
     * @return true if Kafka processing is active, false otherwise.
     */
    public static boolean isKafkaProcessing() {
        return isKafkaProcessing.get();
    }
}
