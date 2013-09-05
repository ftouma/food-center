package foodcenter.service.enums;

public enum ServiceType
{
    TAKE_AWAY("Take Away"),
    TABLE("Table"),
    DELIVERY("Delivery");
        
    
    private final String name;
    
    private ServiceType(String name)
    {
        this.name = name;
    }
    
    
    public String getName() 
    {
        return name;
    };
    
    public static ServiceType forName(String name)
    {
        for (ServiceType s : ServiceType.values())
        {
            if (s.getName().equals(name))
            {
                return s;
            }
        }
        return null;
    }
    
    
    
    
}
