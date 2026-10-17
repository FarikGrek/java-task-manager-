/**
 * Приоритет задачи.
 */
public enum Priority {
    LOW("низкий"),
    MEDIUM("средний"),
    HIGH("высокий");

    private final String title;

    Priority(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    /** Преобразует число 1-3 в приоритет. Возвращает null, если число некорректное. */
    public static Priority fromNumber(int n) {
        switch (n) {
            case 1: return LOW;
            case 2: return MEDIUM;
            case 3: return HIGH;
            default: return null;
        }
    }
}
