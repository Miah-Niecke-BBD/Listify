

 export async function GetJwtToken<LoginResponse>( code:string): Promise<LoginResponse | null> {
  try {
    const response = await fetch("http://localhost:8080/authentication?code="+code);
    
    if (!response.ok) {
      throw new Error(`Response status: ${response.status}`);
    }

    const data: LoginResponse = await response.json();
    return data;
  } catch (error: unknown) {
    if (error instanceof Error) {
      console.error(error.message);
    } else {
      console.error("An unknown error occurred.");
    }
    return null;
  }
}

export async function GetAuthCode<AuthCodeResponse> ():Promise<AuthCodeResponse | null> {
  try{
    const response = await fetch("http://localhost:8080/login");

    if (!response.ok) {
      throw new Error(`Response status: ${response.status}`);
    }

    const data: AuthCodeResponse = await response.json();
    return data;
  }catch(error: unknown){
      if (error instanceof Error) {
        console.error(error.message);
      } else {
        console.error("An unknown error occurred.");
      }
      return null;
  }
}

