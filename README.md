# Port Management System

A comprehensive system designed for managing ports, vehicles, and containers efficiently. This application enables users to perform CRUD operations, manage logistics, and track port-related activities.

## Tech Stack

- Java  

## Features

- **User Management**:  
  - Predefined users can log in with a username and password.  
  - Users can view, modify, and process relevant entities (ports, containers, vehicles).  
  - Admin users have full system access, while port managers handle only containers.  

- **CRUD Operations**:  
  - Vehicles, ports, containers, and managers can be added, updated, and removed from the system.  
  - Full implementation of Create, Read, Update, and Delete (CRUD) functionalities.  

- **Vehicle Operations**:  
  - Vehicles can load and unload containers.  
  - Vehicles can determine if they can successfully move to a port with their current load.  
  - Vehicles can move between ports.  
  - Vehicles can be refueled.  

- **Port and Logistics Management**:  
  - List all ships docked at a port.  
  - Track and manage all trips scheduled within a given day.  
  - Retrieve trip history for a specific date range.  

- **Statistical Operations**:  
  - Calculate total fuel consumption for a given day.  
  - Determine the total weight of each container type.  
