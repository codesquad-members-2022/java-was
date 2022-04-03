package db;

import model.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CommentDataBase {

    private static final List<Comment> comments = Collections.synchronizedList(new ArrayList<>());

    public static void add(Comment comment) {
        comments.add(comment);
    }

    public static List<Comment> findRecent(int count) {
        return comments.stream()
                .sorted(Comparator.comparing(Comment::getTimestamp).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    static {
        add(new Comment("javajigi", "시간이 없다"));
        add(new Comment("Sammy", "뇌가 없다"));
        add(new Comment("ikjo", "굿바이"));
        add(new Comment("honux", "이 쪽이 위에 보여야 함"));
    }
}
