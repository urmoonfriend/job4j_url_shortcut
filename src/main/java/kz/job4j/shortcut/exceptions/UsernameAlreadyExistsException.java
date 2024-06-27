package kz.job4j.shortcut.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException() {
        super("Такой username уже существует");
    }
}
