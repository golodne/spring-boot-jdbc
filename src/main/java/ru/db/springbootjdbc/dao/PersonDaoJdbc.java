package ru.db.springbootjdbc.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.db.springbootjdbc.model.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PersonDaoJdbc implements PersonDao {

    private NamedParameterJdbcOperations jdbc;

    public PersonDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbc = namedParameterJdbcOperations;
    }

    @Override
    public int count() {
        Map<String, Object> mapA = new HashMap<>(1);
        return jdbc.queryForObject("select count(*) from persons",mapA, Integer.class);
    }

    @Override
    public void insert(Person person) {
        Map<String, Object> mapA = new HashMap<>(1);
        mapA.put("id", person.getId());
        mapA.put("name", person.getName());
        jdbc.update("insert into persons (id,`name`) values (:id, :name)", mapA);

        Map<String, Object> mapB = new HashMap<>(1);
        person.getEmail().stream().forEach(x ->
        {
            mapB.put("person_id", person.getId());
            mapB.put("email", x);
            jdbc.update("insert into email (person_id, `email`) values(:person_id, :email)", mapB);
        });
    }

    @Override
    public Person getById(int id) {
        Map<String, Object> mapA = new HashMap<>(1);
        mapA.put("id", id);
        // return jdbc.queryForObject("select * from persons where id = :id",mapA, new PersonMapper());
        return jdbc.query("select persons.id, persons.name, email.email from persons " +
                "left join email on persons.id = email.person_id where (persons.id = :id)",mapA, new PersonExtractorMapper());
    }

    private static class PersonMapper implements RowMapper<Person> {
        @Override
        public Person mapRow(ResultSet resultSet, int i) throws SQLException {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            return new Person(id,name, null);
        }
    }

    private static class  PersonExtractorMapper implements ResultSetExtractor<Person> {

        @Override
        public Person extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            List<String> emailList = new ArrayList<>();
            Person p = null;
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                if (p == null) p = new Person(id, name, emailList);
                emailList.add(email);
            }
            return p;
        }
    }
}
