export async function GetUser<UserInterface>(jwtToken: string|null): Promise<{ user: UserInterface | null, error?: string }> {
  try {
    const response = await fetch("http://localhost:8080/user", {
      headers: {
        "Authorization": `Bearer ${jwtToken}`,
      },
    });

    const data = await response.json();

    if (!response.ok) {
     
      return { user: null, error: data.message || 'Unknown error' };
    }

    return { user: data };
  } catch (error) {
    return { user: null, error: 'Network or unexpected error' };
  }
}

export async function CreateUser<UserInterface>(jwtToken: string|null): Promise<{ user: UserInterface | null, error?: string }> {
  try {
    const response = await fetch("http://localhost:8080/user/create", {
      headers: {
        "Authorization": `Bearer ${jwtToken}`,
      },
    });

    const data = await response.json();

    if (!response.ok) {
     
      return { user: null, error: data.message || 'Unknown error' };
    }

    return { user: data };
  } catch (error) {
    return { user: null, error: 'Network or unexpected error' };
  }
}
  

