package grabber.store;

import grabber.Store;
import grabber.model.Post;

import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection conn;

    public static void main(String[] args) {

        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            in.close();

            PsqlStore store = new PsqlStore(config);

            Post post = new Post("name", "text", "link", new java.util.Date());

//            store.save(post);

            System.out.println(post);

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }


    }

    public PsqlStore(Properties cfg) {
        try {

//            System.out.println(cfg.getProperty("jdbc.driver"));
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

//        try {
//            this.conn = DriverManager.getConnection(cfg.getProperty("jdbc.url"), cfg);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement = conn.prepareStatement(
            "INSERT INTO item(name, text, link, created_at) "
                + "VALUES(?,?,?,?)",
            Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getName());
            statement.setString(2, post.getText());
            statement.setString(3, post.getLink());
            statement.setDate(4, (Date) post.getCreatedAt());
            statement.executeUpdate();

            ResultSet rsl = statement.getGeneratedKeys();
            rsl.next();
            post.setId(rsl.getInt(1));

        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        return null;
    }

    @Override
    public Post findById(String id) {
        return null;
    }

    @Override
    public void close() throws Exception {
        if (conn != null) {
            conn.close();
        }
    }
}
