package service;

import model.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    protected static HashMap<Integer, Node<Task>> nodes = new HashMap<>();
    protected static Node<Task> tail;
    protected static Node<Task> head;

    @Override
    public void clearHistory(){

        nodes.clear();
        head = null;
        tail = null;
    }

    @Override
    public void add(Task task) {

        linkLast(task);
    }

    @Override
    public void remove(int id) {

        if(nodes.containsKey(id)) {

            removeNode(nodes.get(id));
        }
    }

    @Override
    public List<Task> getHistory() {

        return getTasks();
    }

    /**
     * Проходит по ссылкам по всем @Node от @head до @tail. Из каждого Node берет значение task и помещает
     * в список @tasks в порядке следования. Возвращает список @tasks
     * @return
     */
    public List<Task> getTasks() {

        List<Task> tasks = new ArrayList<>();
        Node<Task> node = head;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }
        return tasks;
    }

    /**
     * Получает на входе @task, на ее основе делает новый Node и помещает его в конец списка.
     * @param task
     */
    public void linkLast(Task task) {

        if(nodes.containsKey(task.getId())) {

            removeNode(nodes.get(task.getId()));
        }

        Node<Task> oldTail = tail;
        Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;

        if (oldTail == null) {

            head = newNode;

        }
        else {

            oldTail.next = newNode;
        }

        nodes.put(task.getId(), newNode);
    }

    /**
     * Получает на вход @node и вырезает его из списка. Перепривязывает ссылки соседних @Node друг на друга.
     * @param node
     */
    public void removeNode(Node<Task> node) {

           if (node.equals(head) && nodes.size() == 1) {

               nodes.remove(node.task.getId());
               tail = null;
               head = null;
               return;

           }
           else if (node == head) {

                node.next.prev = null;
                head = node.next;

            }
           else if (node == tail) {

                node.prev.next = null;
                tail = node.prev;

            }
           else {

                node.prev.next = node.next;
                node.next.prev = node.prev;

            }

        nodes.remove(node.task.getId());

    }
}
