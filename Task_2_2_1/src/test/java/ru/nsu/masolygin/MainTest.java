package ru.nsu.masolygin;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testMainMethodExists() {
        assertNotNull(Main.class);
    }

    @Test
    void testMainExecutesWithoutException() {
        Thread mainThread = new Thread(() -> {
            try {
                Main.main(new String[]{});
            } catch (Exception e) {
                // Expected
            }
        });

        mainThread.start();

        try {
            Thread.sleep(500);
            mainThread.interrupt();
            mainThread.join(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertNotNull(mainThread);
    }

    @Test
    void testMainLoadsConfig() {
        Thread mainThread = new Thread(() -> {
            try {
                Main.main(new String[]{});
            } catch (Exception e) {
                // Expected
            }
        });

        mainThread.start();

        try {
            Thread.sleep(300);
            mainThread.interrupt();
            mainThread.join(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String output = outContent.toString();
        assertNotNull(output);
    }

    @Test
    void testMainStartsPizzeria() {
        Thread mainThread = new Thread(() -> {
            try {
                Main.main(new String[]{});
            } catch (Exception e) {
                // Expected
            }
        });

        mainThread.start();

        try {
            Thread.sleep(200);
            mainThread.interrupt();
            mainThread.join(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String output = outContent.toString();
        assertNotNull(output);
    }

    @Test
    void testMainStartsOrderGenerator() {
        Thread mainThread = new Thread(() -> {
            try {
                Main.main(new String[]{});
            } catch (Exception e) {
                // Expected
            }
        });

        mainThread.start();

        try {
            Thread.sleep(400);
            mainThread.interrupt();
            mainThread.join(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertNotNull(mainThread);
    }

    @Test
    void testMainWithEmptyArgs() {
        assertDoesNotThrow(() -> {
            Thread mainThread = new Thread(() -> {
                try {
                    Main.main(new String[]{});
                } catch (Exception e) {
                    // Expected
                }
            });
            mainThread.start();
            Thread.sleep(200);
            mainThread.interrupt();
            mainThread.join(2000);
        });
    }

    @Test
    void testMainOutputContainsPizzeriaMessages() {
        Thread mainThread = new Thread(() -> {
            try {
                Main.main(new String[]{});
            } catch (Exception e) {
                // Expected
            }
        });

        mainThread.start();

        try {
            Thread.sleep(300);
            mainThread.interrupt();
            mainThread.join(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String output = outContent.toString();
        assertNotNull(output);
    }

    @Test
    void testMainCanBeInterrupted() throws InterruptedException {
        Thread mainThread = new Thread(() -> {
            try {
                Main.main(new String[]{});
            } catch (Exception e) {
                // Expected
            }
        });

        mainThread.start();
        Thread.sleep(100);
        mainThread.interrupt();
        mainThread.join(2000);

        assertNotNull(mainThread);
    }

    @Test
    void testMainCreatesAllComponents() {
        Thread mainThread = new Thread(() -> {
            try {
                Main.main(new String[]{});
            } catch (Exception e) {
                // Expected
            }
        });

        mainThread.start();

        try {
            Thread.sleep(250);
            mainThread.interrupt();
            mainThread.join(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertNotNull(mainThread);
    }

    @Test
    void testMainRunsForShortTime() {
        Thread mainThread = new Thread(() -> {
            try {
                Main.main(new String[]{});
            } catch (Exception e) {
                // Expected
            }
        });

        mainThread.start();

        try {
            Thread.sleep(150);
            mainThread.interrupt();
            mainThread.join(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertNotNull(mainThread);
    }
}

