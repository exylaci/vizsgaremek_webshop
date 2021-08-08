package webshop.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class NotFindException extends AbstractThrowableProblem {

    public NotFindException(String url, String message) {
        super(
                URI.create(url),
                message,
                Status.NOT_ACCEPTABLE,
                message);
    }
}