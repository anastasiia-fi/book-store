package dao;

public interface SpecificationProviderManager<T> {
  SpecificationProvider<T> getSpecificationProvider(String key);
}
