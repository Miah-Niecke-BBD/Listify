import { removeJwt } from "./Main";

export async function GetUser<UserInterface>(jwtToken: string|null): Promise<{ user: UserInterface | null, error?: string }> {
  try {
    const response = await fetch("http://localhost:8080/user", {
      headers: {
        "Authorization": `Bearer ${jwtToken}`,
      },
    });

    const data = await response.json();
    if(response.status === 401) {
      removeJwt();
    }
  console.log(data)
    return { user: data };
  } catch (error:any) {

    console.log((`Failed to get User Details (status ${error.status}): ${error.message}`))
    throw error;
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
    if(response.status === 401) {
      removeJwt();
    }

    return { user: data };
  } catch (error:any) {

    console.log((`Failed to create User (status ${error.status}): ${error.message}`))
    throw error;
  }
}


