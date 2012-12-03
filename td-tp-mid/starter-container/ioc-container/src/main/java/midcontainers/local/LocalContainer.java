package midcontainers.local;

import java.lang.*;
import java.lang.reflect.*;
import java.util.*;
import midcontainers.Binding;
import midcontainers.Binding.*;
import midcontainers.Container;
import midcontainers.ContainerException;
import midcontainers.Named;

public class LocalContainer implements Container {

  private final Map<String, Object> nameBinding = new HashMap<String, Object>();

  private Map<Key, Binding> keyBinding = new HashMap<Key, Binding>();

  public Container define(String name, Object value)
  {
    System.out.println("Define a value. \n");
    nameBinding.put(name, value);
    return this;
  }

  public Container declare(Binding binding)
  {
    System.out.println("Declare a component binding. \n");
    keyBinding.put(binding.getKey(), binding);
    return this;
  }

  public <T> T obtainReference(Class<T> interfaceClass)
  {
    return obtainReference(interfaceClass, null);
  }

  public <T> T obtainReference(Class<T> interfaceClass, String qualifier)
  {

    Key key = new Key(interfaceClass, qualifier);

    if (keyBinding.containsKey(key) == false) {
      throw new ContainerException("Pas de valeur correspondant pour " + interfaceClass.getName());
    }       

    Binding binding = keyBinding.get(key);

    Class<?> implementation = binding.getImplementationClass();

    if (implementation == null) {
      throw new ContainerException("Pas d'implémentation pour " + interfaceClass.getName() + ".");
    }
    
    for (Constructor<?> constructor : implementation.getConstructors()){

      Class<?> [] parameterTypes = constructor.getParameterTypes();
      Object [] parameters = new Object[parameterTypes.length];
      try {
        for(int i = 0; i < parameterTypes.length; i++) {
		  System.out.println("step -> parameterTypes["+i+"] : "+parameterTypes[i]);
          Class<?> type = parameterTypes[i];
          parameters[i] = obtainReference(type);
          System.out.println("Reference obtained");
        }
        return (T) constructor.newInstance(parameters);
      }catch (Exception e) {}
       
    }

    throw new ContainerException("Aucun constructeur trouvé pour satisfaire la demande " + interfaceClass.getName() + " / " + implementation.getName());

  }

  public Object definitionValue(String name)
  {
    System.out.println("Obtain a defined value. \n");
    if(nameBinding.get(name)==null)
    {
      throw new ContainerException();
    }
    else
    {
      Object MyObject = nameBinding.get(name);
      return MyObject;
    }
  }

  public boolean hasReferenceDeclaredFor(Class<?> interfaceClass, String qualifier)
  {
    System.out.println("Checks whether a component is available locally for an interface with a qualifier.");
    return true;
  }

  public boolean hasReferenceDeclaredFor(Class<?> interfaceClass)
  {
    System.out.println("Checks whether a component is available locally for an interface with a <code>null</code> qualifier. \n");
    return true;
  }

  public boolean hasValueDefinedFor(String name)
  {
    System.out.println("Checks whether a value is available locally.\n");
    return true;
  }

  public Container delegateTo(Container container)
  {
    System.out.println("Add a delegation link from this container to another one \n");
    return container;
  }

}
