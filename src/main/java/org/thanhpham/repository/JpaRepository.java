package org.thanhpham.repository;

import org.thanhpham.entity.Page;
import org.thanhpham.entity.Pageable;
import org.thanhpham.entity.Sort;
import org.thanhpham.service.IGoogleSheetClient;
import org.thanhpham.util.ConvertToIndex;
import org.thanhpham.util.GenericMapper;
import org.thanhpham.util.ListUtil;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.StreamSupport;

public abstract class JpaRepository<P,T> implements CRUDRepository<P,T>, PagingAndSortingRepository<P,T> {
    protected final IGoogleSheetClient client;
    protected final Class<P> entityClass;
    protected final Integer field;
    protected final GenericMapper<P> genericMapper;

    @SuppressWarnings("unchecked")
    public JpaRepository(IGoogleSheetClient client) {
        this.client = client;
        this.entityClass = (Class<P>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
        field = entityClass.getDeclaredFields().length;
        genericMapper = new GenericMapper<>(entityClass);
    }

    @Override
    public Optional<P> findById(T id) throws IOException {
        List<Object> data =  client.findById(ConvertToIndex.getCharacter(0) + ":" + ConvertToIndex.getCharacter(field - 1)
                , String.valueOf(ConvertToIndex.getCharacter(GenericMapper.getIndexOfIdField(entityClass))), id.toString(), true);
        if(data.isEmpty()){ return Optional.empty(); }
        return Optional.of(genericMapper.mapFromList(data));
    }

    @Override
    public List<P> findAllById(Iterable<T> ids) throws IOException {
        Set<T> iteratorValues = new HashSet<>();
        ids.forEach(iteratorValues::add);

        return client.readSheet(ConvertToIndex.getCharacter(0) + ":" + ConvertToIndex.getCharacter(field - 1)).stream().map(
                genericMapper::mapFromList
        ).filter(
                t -> iteratorValues.contains(GenericMapper.getIdFromEntity(t))
        ).toList();
    }

    @Override
    public P save(P entity) throws IOException {
        Optional<P> result = findById((T) GenericMapper.getIdFromEntity(entity));
        if(result.isPresent()){
            throw new RuntimeException("Entity already existed");
        }
        client.appendRow(genericMapper.mapFromEntity(entity));
        return entity;
    }

    @Override
    public void deleteById(T id) throws IOException {
        client.deleteById(client.getSheetId(), ConvertToIndex.getCharacter(GenericMapper.getIndexOfIdField(entityClass)).toString(), id.toString());
    }

    @Override
    public List<P> findALl() throws IOException {
        List<List<Object>> data = client.readSheet(ConvertToIndex.getCharacter(0) + ":" + ConvertToIndex.getCharacter(field - 1));
        return data.stream().map(genericMapper::mapFromList).toList();
    }

    @Override
    public List<P> saveAll(Iterable<P> entities) throws IOException {
        List<List<Object>> result = StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(entities.iterator(), Spliterator.ORDERED), false)
                .map(genericMapper::mapFromEntity)
                .toList();
        client.appendRows(result);
        return result.stream().map(genericMapper::mapFromList).toList();
    }

    @Override
    public boolean existsById(T id, String cell) throws IOException {
        return client.existById((String) id, cell, ConvertToIndex.getCharacter(GenericMapper.getIndexOfIdField(entityClass)).toString()) != -1;
    }

    @Override
    public void delete(P entity) throws IOException {
        client.deleteById(client.getSheetId(), GenericMapper.getIdFromEntity(entity).toString(),ConvertToIndex.getCharacter(GenericMapper.getIndexOfIdField(entityClass)).toString());
    }

    public P update(P entity, String cell) throws IOException {
        Integer index = client.existById(GenericMapper.getIdFromEntity(entity).toString(), cell, ConvertToIndex.getCharacter(GenericMapper.getIndexOfIdField(entityClass)).toString());
        if(index != -1){
            client.updateRow(ConvertToIndex.getCharacter(0) + index.toString() + ":" + ConvertToIndex.getCharacter(field - 1) + index, genericMapper.mapFromEntity(entity));
            return entity;
        }else {
            throw new RuntimeException("Entity not found");
        }
    }

    public List<P> findAll(String column, String keyword, boolean match) throws IOException {
        return client.findAll(ConvertToIndex.getCharacter(0) + ":" + ConvertToIndex.getCharacter(field - 1), column, keyword, match).stream().map(
                genericMapper::mapFromList
        ).toList();
    }

    public void deleteAll(String column, String keyword) throws IOException {
        client.deleteAll(client.getSheetId(), column, keyword);
    }

    public List<P> filterByKeyword(String column, String keyword, boolean match) throws IOException {
        return client.filterByKeyword(ConvertToIndex.getCharacter(0) + ":" + ConvertToIndex.getCharacter(field - 1), column, keyword, match).stream().map(
                genericMapper::mapFromList
        ).toList();
    }

    public List<Map.Entry<Integer, P>> findRowsWithIndex(String column, String keyword, boolean match, boolean findAll) throws IOException, InterruptedException {
        return client.findRowsWithIndex(ConvertToIndex.getCharacter(0) + ":" + ConvertToIndex.getCharacter(field - 1), column, keyword, match, findAll)
                .stream().map(
                        t -> Map.entry(t.getKey(), genericMapper.mapFromList(t.getValue())))
                .toList();
    }

    @Override
    public Page<P> findAll(Pageable pageable) throws IOException {
        List<P> result = client.readSheet(ConvertToIndex.getCharacter(0) + ":" + ConvertToIndex.getCharacter(field - 1))
                .stream().map(
                        genericMapper::mapFromList
                ).toList();
        if(pageable.getSort() != null && pageable.getSort().getProperty() != null) {
            ListUtil.sortList(result, pageable.getSort());
        }

        return ListUtil.paginate(result, pageable);
    }

    @Override
    public List<P> findAll(Sort sort) throws IOException {
        List<P> result = client.readSheet(ConvertToIndex.getCharacter(0) + ":" + ConvertToIndex.getCharacter(field - 1)).stream().map(
                genericMapper::mapFromList
        ).toList();

        ListUtil.sortList(result, sort);
        return result;
    }

    public P update(P entity) throws IOException, InterruptedException {
        String id = (String) GenericMapper.getIdFromEntity(entity);
        List<Integer> result = client.findIndex(String.valueOf(ConvertToIndex.getCharacter(GenericMapper.getIndexOfIdField(entityClass))), id, true, false);
        if(result.isEmpty())
        {
            throw new RuntimeException("Entity with id = " + id + " not found");
        }
        Integer index = result.getFirst();
        System.out.println(index);
        client.updateRow(ConvertToIndex.getCharacter(0) + index.toString() + ":" + ConvertToIndex.getCharacter(field - 1) + index, genericMapper.mapFromEntity(entity));
        return entity;
    }
}
