import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Консольный менеджер задач. Запуск: java Main
 */
public class Main {
    public static void main(String[] args) {
        // Чтобы русский текст корректно отображался в любой консоли
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        TaskManager manager = new TaskManager(Paths.get("tasks.txt"));
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

        System.out.println("=== Менеджер задач ===");
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    showTasks(manager);
                    break;
                case "2":
                    addTask(manager, scanner);
                    break;
                case "3":
                    completeTask(manager, scanner);
                    break;
                case "4":
                    deleteTask(manager, scanner);
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Такого пункта нет, выберите цифру из меню.");
            }
        }
        System.out.println("Задачи сохранены. До встречи!");
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1 - Показать задачи");
        System.out.println("2 - Добавить задачу");
        System.out.println("3 - Отметить выполненной");
        System.out.println("4 - Удалить задачу");
        System.out.println("0 - Выход");
        System.out.print("Выбор: ");
    }

    private static void showTasks(TaskManager manager) {
        List<Task> tasks = manager.getAllSorted();
        if (tasks.isEmpty()) {
            System.out.println("Список пуст. Добавьте первую задачу!");
            return;
        }
        for (Task task : tasks) {
            System.out.println(task);
        }
        System.out.println("Выполнено: " + manager.countDone() + " из " + manager.countAll());
    }

    private static void addTask(TaskManager manager, Scanner scanner) {
        System.out.print("Название задачи: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("Название не может быть пустым.");
            return;
        }
        System.out.print("Приоритет (1 - низкий, 2 - средний, 3 - высокий): ");
        Integer number = readNumber(scanner);
        Priority priority = number == null ? null : Priority.fromNumber(number);
        if (priority == null) {
            System.out.println("Нужно ввести 1, 2 или 3. Задача не добавлена.");
            return;
        }
        Task task = manager.add(title, priority);
        System.out.println("Добавлено: " + task);
    }

    private static void completeTask(TaskManager manager, Scanner scanner) {
        System.out.print("Номер задачи: ");
        Integer id = readNumber(scanner);
        if (id != null && manager.complete(id)) {
            System.out.println("Готово, задача выполнена!");
        } else {
            System.out.println("Задача с таким номером не найдена.");
        }
    }

    private static void deleteTask(TaskManager manager, Scanner scanner) {
        System.out.print("Номер задачи: ");
        Integer id = readNumber(scanner);
        if (id != null && manager.delete(id)) {
            System.out.println("Задача удалена.");
        } else {
            System.out.println("Задача с таким номером не найдена.");
        }
    }

    /** Читает число из строки. Если ввели не число, возвращает null. */
    private static Integer readNumber(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
