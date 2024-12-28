package mp.jprime.concurrent;

/**
 * Задача с приоритетом выполнения
 */
public interface PriorityTask extends Runnable, Comparable<PriorityTask> {

}
