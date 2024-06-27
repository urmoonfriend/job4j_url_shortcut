package kz.job4j.shortcut.exceptions;

public class SiteAlreadyExistsException extends RuntimeException {
    public SiteAlreadyExistsException() {
        super("Сайт уже существует");
    }
}
