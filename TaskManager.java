import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Хранит список задач и умеет сохранять его в файл и загружать обратно.
 */
public class TaskManager {
    private final List<Task> tasks = new ArrayList<>();
    private final Path file;
    private int nextId = 1;

    public TaskManager(Path file) {
        this.file = file;
        load();
    }

    public Task add(String title, Priority priority) {
        Task task = new Task(nextId++, title, priority, false);
        tasks.add(task);
        save();
        return task;
    }

    /** Отмечает задачу выполненной. Возвращает false, если такой задачи нет. */
    public boolean complete(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.markDone();
                save();
                return true;
            }
        }
        return false;
    }

    /** Удаляет задачу. Возвращает false, если такой задачи нет. */
    public boolean delete(int id) {
        boolean removed = tasks.removeIf(task -> task.getId() == id);
        if (removed) {
            save();
        }
        return removed;
    }

    /** Все задачи: сначала невыполненные, внутри по убыванию приоритета. */
    public List<Task> getAllSorted() {
        List<Task> sorted = new ArrayList<>(tasks);
        sorted.sort(Comparator
                .comparing(Task::isDone)
                .thenComparing(Task::getPriority, Comparator.reverseOrder())
                .thenComparingInt(Task::getId));
        return sorted;
    }

    public int countDone() {
        return (int) tasks.stream().filter(Task::isDone).count();
    }

    public int countAll() {
        return tasks.size();
    }

    private void load() {
        if (!Files.exists(file)) {
            return;
        }
        try {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                Task task = Task.fromFileLine(line);
                if (task != null) {
                    tasks.add(task);
                    nextId = Math.max(nextId, task.getId() + 1);
                }
            }
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл задач: " + e.getMessage());
        }
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(task.toFileLine());
        }
        try {
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Не удалось сохранить задачи: " + e.getMessage());
        }
    }
}
