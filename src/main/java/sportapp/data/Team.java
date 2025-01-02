package sportapp.data;

public record Team(String name, Integer score) {

    public Team {
        if (score == null) {
            throw new IllegalArgumentException("Score is null!");
        }
        if (score < 0) {
            throw new IllegalArgumentException("Score can't be a negative number!");
        }
    }
}
