package iths.theroom.repository;

import iths.theroom.entity.RoleEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    RoleEntity roleEntity;
    RoleEntity roleEntity2;


    @Before
    public void setUp() {

       roleEntity = new RoleEntity(RoleEntity.Role.GLOBAL_ADMIN);
       roleEntity2 = new RoleEntity(RoleEntity.Role.MODERATOR);

    }

    @Test
    public void getRoles() {
        entityManager.persist(roleEntity);
        entityManager.persist(roleEntity2);
        List<RoleEntity> results = roleRepository.findByRoles(List.of(RoleEntity.Role.GLOBAL_ADMIN, RoleEntity.Role.MODERATOR));
        assertThat(results.size()).isEqualTo(2);
    }
}
