package kz.job4j.shortcut.exceptions;

public class SiteNotFoundException extends RuntimeException {
    public SiteNotFoundException() {
        super("Site не найден");
    }
}
