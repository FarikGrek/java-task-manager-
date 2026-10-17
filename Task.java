/**
 * Одна задача: номер, название, приоритет и статус выполнения.
 */
public class Task {
    private static final String SEPARATOR = "\t";

    private final int id;
    private final String title;
    private final Priority priority;
    private boolean done;

    public Task(int id, String title, Priority priority, boolean done) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Priority getPriority() {
        return priority;
    }

    public boolean isDone() {
        return done;
    }

    public void markDone() {
        this.done = true;
    }

    /** Строка для сохранения в файл. */
    public String toFileLine() {
        return id + SEPARATOR + done + SEPARATOR + priority.name() + SEPARATOR + title;
    }

    /** Создаёт задачу из строки файла. Если строка повреждена, возвращает null. */
    public static Task fromFileLine(String line) {
        String[] parts = line.split(SEPARATOR, 4);
        if (parts.length < 4) {
            return null;
        }
        try {
            int id = Integer.parseInt(parts[0]);
            boolean done = Boolean.parseBoolean(parts[1]);
            Priority priority = Priority.valueOf(parts[2]);
            return new Task(id, parts[3], priority, done);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("[%s] #%d %s (приоритет: %s)",
                done ? "x" : " ", id, title, priority.getTitle());
    }
}
