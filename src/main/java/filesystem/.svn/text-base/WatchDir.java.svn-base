package filesystem;

import concurrent.Task;
import concurrent.TaskChain;
import receipts.Receipt;
import receipts.tasks.ParseReceipt;
import receipts.tasks.ReadReceipt;
import receipts.tasks.ReadReceiptNotifyee;
import receipts.tasks.RecognizeReceipt;

import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import static util.props.CrawlerProperties.PROPS_WITH_ENV;
import static util.props.PropertyTags.RECEIPTS_INCOMING_DIR;

import java.nio.file.attribute.*;
import java.io.*;
import java.util.*;

public class WatchDir {
    private final WatchService watcher;
    private final Map<WatchKey,Path> keys;
    private final boolean recursive;
    private boolean trace = false;
    private static final Path INCOMING_DIR = Paths.get(PROPS_WITH_ENV.getProperty(RECEIPTS_INCOMING_DIR, "C:\\Temp\\Receipts_Test\\incoming"));

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    /**
     * Register the given directory with the WatchDir
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE/*, ENTRY_DELETE, ENTRY_MODIFY*/);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchDir.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException
            {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Creates a WatchDir and registers the given directory
     */
    WatchDir(Path dir, boolean recursive) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey,Path>();
        this.recursive = recursive;

        if (recursive) {
            System.out.format("Scanning %s ...\n", dir);
            registerAll(dir);
            System.out.println("Done.");
        } else {
            register(dir);
        }

        // enable trace after initial registration
        this.trace = true;
    }

    /**
     * Process all events for keys queued to the watcher
     */
    void processEvents() throws IOException {
        for (;;) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                // print out event
                System.out.format("%s: %s\n", event.kind().name(), child);
                Receipt r = new Receipt(child);
                List<Task> tasks = new ArrayList<Task>();
                tasks.add(new ReadReceipt(null, r));
                tasks.add(new RecognizeReceipt(null, r));
                tasks.add(new ParseReceipt(null, r));
                TaskChain taskChain = new TaskChain(tasks);
                //run a new thread
                (new Thread(taskChain)).start();

                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (recursive && (kind == ENTRY_CREATE)) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        // ignore to keep sample readbale
                    }
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // parse arguments
        boolean recursive = false;
        // register directory and process its events
        new WatchDir(INCOMING_DIR, recursive).processEvents();
    }
}