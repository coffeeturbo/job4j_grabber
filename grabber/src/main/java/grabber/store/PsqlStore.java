package grabber.store;

import grabber.Store;
import grabber.model.Post;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class PsqlStore implements Store, AutoCloseable {

    private Connection conn;

    public static void main(String[] args) {

        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            in.close();

            PsqlStore store = new PsqlStore(config);

            int randomNum = (0 + (int) (Math.random() * 1000));
            Post post = new Post("name", "text", "link" + randomNum, new java.util.Date());

            store.save(post);
            System.out.println("Outputting findById post");
            System.out.println(store.findById("1"));
            System.out.println("Outputting single post");
            System.out.println(post);
            System.out.println("Outputting All posts");
            store.getAll().forEach(System.out::println);



        } catch (Exception e) {
            throw new IllegalStateException(e);
        }


    }

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        try {
            this.conn = DriverManager.getConnection(
                cfg.getProperty("jdbc.url"),
                cfg.getProperty("jdbc.username"),
                cfg.getProperty("jdbc.password")
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement = conn.prepareStatement(
            "INSERT INTO post(name, text, link, created_at) "
                + "VALUES(?,?,?,?)",
            Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getName());
            statement.setString(2, post.getText());
            statement.setString(3, post.getLink());
            statement.setDate(4, new java.sql.Date(post.getCreatedAt().getTime()));
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
        List<Post> list = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement(
            "SELECT id, name, text, link, created_at FROM post")) {
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                list.add(new Post(
                    result.getInt(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getDate(5)
                ));
            }

        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

        return list;
    }

    @Override
    public Post findById(String id) {
        Post item = null;
        try (PreparedStatement statement = conn.prepareStatement(
            "SELECT id, name, text, link, created_at FROM post WHERE id = ?")) {
            statement.setInt(1, Integer.parseInt(id));
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                item = new Post(
                    result.getInt(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getDate(5)
                );
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }

        return item;
    }

    @Override
    public void close() throws Exception {
        if (conn != null) {
            conn.close();
        }
    }
}
