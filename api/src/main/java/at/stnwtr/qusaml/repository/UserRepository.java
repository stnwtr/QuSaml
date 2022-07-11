package at.stnwtr.qusaml.repository;

import at.stnwtr.qusaml.entity.User;
import at.stnwtr.qusaml.factory.CrudQueryFactory;
import at.stnwtr.qusaml.factory.UserQueryFactory;

public class UserRepository implements CrudRepository<User, Integer> {
    private final UserQueryFactory userQueryFactory;

    public UserRepository(UserQueryFactory userQueryFactory) {
        this.userQueryFactory = userQueryFactory;
    }

    @Override
    public CrudQueryFactory<User, Integer> queryFactory() {
        return userQueryFactory;
    }
}
