import { removeJwt } from "./Main";

export async function GetUser<UserInterface>(jwtToken: string | null): Promise<{ user: UserInterface | null, error?: string }> {
  try {
    const response = await fetch("http://localhost:8080/user", {
      headers: {
        "Authorization": `Bearer ${jwtToken}`,
      },
    });

    const data = await response.json();
    if (response.status === 401) {
      removeJwt();
    }

    return { user: data };
  } catch (error: any) {

    console.log((`Failed to get User Details (status ${error.status}): ${error.message}`))
    throw error;
  }
}

export async function CreateUser<UserInterface>(jwtToken: string | null): Promise<{ user: UserInterface | null, error?: string }> {
  try {
    const response = await fetch("http://localhost:8080/user/create", {
      headers: {
        "Authorization": `Bearer ${jwtToken}`,
      },
    });

    const data = await response.json();
    if (response.status === 401) {
      removeJwt();
    }

    return { user: data };
  } catch (error: any) {

    console.log((`Failed to create User (status ${error.status}): ${error.message}`))
    throw error;
  }
}
export async function getAllUserIDs(): Promise<string[] | null> {
  const jwtToken: string | null = localStorage.getItem("jwtToken");
  try {
    const response = await fetch("http://localhost:8080/user/all", {
      headers: {
        "Authorization": `Bearer ${jwtToken}`,
      },
    });

    const data = await response.json();

    if (response.status === 401) {
      removeJwt();
      return null;
    }

    return data;

  } catch (error: any) {
    console.log(`Failed to fetch users (status ${error.status}): ${error.message}`);
    throw error;
  }
}




