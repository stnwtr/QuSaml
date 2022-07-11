package at.stnwtr.qusaml.repository;

import at.stnwtr.qusaml.entity.Post;
import at.stnwtr.qusaml.factory.CrudQueryFactory;
import at.stnwtr.qusaml.factory.PostQueryFactory;

public class PostRepository implements CrudRepository<Post, Integer> {
    private final PostQueryFactory postQueryFactory;

    public PostRepository(PostQueryFactory postQueryFactory) {
        this.postQueryFactory = postQueryFactory;
    }

    @Override
    public CrudQueryFactory<Post, Integer> queryFactory() {
        return postQueryFactory;
    }
}
