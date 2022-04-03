package util;

import webserver.HttpRequest;

public class Pair {
    private final String key;
    private final String value;

    public Pair(String key, String value) {
        this.key = key.trim();
        this.value = value.trim();
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + key.hashCode();
        result = prime * result + value.hashCode();

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Pair other = (Pair) obj;

        return (key.equals(other.key) && value.equals(other.value));
    }

    @Override
    public String toString() {
        return "Pair [key=" + key + ", value=" + value + "]";
    }
}
