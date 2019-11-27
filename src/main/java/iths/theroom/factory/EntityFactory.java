package iths.theroom.factory;

import java.util.List;

public interface EntityFactory<T, S> {

    T entityToModel(S s);

    List<T> entityToModel(List<S> s);


}
