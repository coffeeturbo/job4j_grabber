package grabber.store;

import grabber.Store;
import grabber.model.Post;

import java.util.ArrayList;
import java.util.List;

public class MemStore implements Store {

    private final List<Post> posts;

    public MemStore() {
        this.posts = new ArrayList<>();
    }

    @Override
    public void save(Post post) {
        this.posts.add(post);
    }

    @Override
    public List<Post> getAll() {
        return posts;
    }

    @Override
    public Post findById(String id) {
        return null;
    }
}
