package grabber;

import grabber.model.Post;

import java.util.List;

public interface Parse {
    List<Post> list(String link);

    Post detail(String link);
}
