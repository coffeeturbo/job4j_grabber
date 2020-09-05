package grabber;

import grabber.model.Post;

import java.util.List;

public interface Store {
    void save(Post post);

    List<Post> getAll();
}
