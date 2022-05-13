package xin.cosmos.common.function;

@FunctionalInterface
public interface ICallbackFunction {
    <T> T call();
}
